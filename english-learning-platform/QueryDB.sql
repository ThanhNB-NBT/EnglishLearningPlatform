-- English Learning Platform - PostgreSQL Database Schema
-- Chạy script này để tạo tất cả bảng cùng lúc

-- ==================== XÓA BẢNG CŨ (nếu có) ====================
DROP TABLE IF EXISTS user_daily_stats CASCADE;
DROP TABLE IF EXISTS user_streaks CASCADE;
DROP TABLE IF EXISTS user_points CASCADE;
DROP TABLE IF EXISTS user_writing_attempts CASCADE;
DROP TABLE IF EXISTS writing_exercises CASCADE;
DROP TABLE IF EXISTS user_speaking_attempts CASCADE;
DROP TABLE IF EXISTS vocabulary_review_sessions CASCADE;
DROP TABLE IF EXISTS user_vocabulary_progress CASCADE;
DROP TABLE IF EXISTS user_question_attempts CASCADE;
DROP TABLE IF EXISTS user_lesson_progress CASCADE;
DROP TABLE IF EXISTS user_course_progress CASCADE;
DROP TABLE IF EXISTS speaking_topics CASCADE;
DROP TABLE IF EXISTS reading_passages CASCADE;
DROP TABLE IF EXISTS listening_passages CASCADE;
DROP TABLE IF EXISTS vocabulary_words CASCADE;
DROP TABLE IF EXISTS vocabulary_topics CASCADE;
DROP TABLE IF EXISTS question_options CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS lessons CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS user_settings CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==================== TẠO BẢNG MỚI ====================

-- Bảng Users (Người dùng)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('ADMIN', 'USER')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng User Settings (Cài đặt người dùng)
CREATE TABLE user_settings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    daily_goal_words INTEGER DEFAULT 10,
    daily_goal_lessons INTEGER DEFAULT 1,
    reminder_time TIME DEFAULT '19:00:00',
    notification_enabled BOOLEAN DEFAULT true
);

-- Bảng Skills (6 kỹ năng: Grammar, Vocabulary, Listening, Reading, Speaking, Writing)
CREATE TABLE skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    has_sequential_unlock BOOLEAN DEFAULT false,
    order_index INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true
);

-- Bảng Courses (Khóa học theo từng skill)
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    skill_id BIGINT REFERENCES skills(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    level VARCHAR(20) DEFAULT 'BEGINNER' CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    order_index INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Lessons (Bài học trong từng course)
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    lesson_type VARCHAR(20) DEFAULT 'THEORY' CHECK (lesson_type IN ('THEORY', 'PRACTICE', 'TEST')),
    order_index INTEGER DEFAULT 0,
    pass_score DECIMAL(3,1) DEFAULT 70.0,
    unlock_condition TEXT,
    is_grammar_sequential BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Questions (Câu hỏi trong bài học)
CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    question_type VARCHAR(50) NOT NULL,
    points DECIMAL(3,1) DEFAULT 1.0,
    order_index INTEGER DEFAULT 0,
    explanation TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Question Options (Đáp án cho câu hỏi trắc nghiệm)
CREATE TABLE question_options (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT REFERENCES questions(id) ON DELETE CASCADE,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT false,
    order_index INTEGER DEFAULT 0
);

-- Bảng Vocabulary Topics (Chủ đề từ vựng)
CREATE TABLE vocabulary_topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    order_index INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true
);

-- Bảng Vocabulary Words (Từ vựng)
CREATE TABLE vocabulary_words (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(100) NOT NULL,
    pronunciation VARCHAR(100),
    part_of_speech VARCHAR(20) NOT NULL,
    meaning_vietnamese TEXT NOT NULL,
    example_sentence TEXT,
    audio_url VARCHAR(255),
    topic_id BIGINT REFERENCES vocabulary_topics(id) ON DELETE SET NULL,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM' CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Listening Passages (Bài nghe)
CREATE TABLE listening_passages (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    audio_url VARCHAR(255) NOT NULL,
    level VARCHAR(20) DEFAULT 'BEGINNER' CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Reading Passages (Bài đọc)
CREATE TABLE reading_passages (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    level VARCHAR(20) DEFAULT 'BEGINNER' CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    genre VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Speaking Topics (Chủ đề nói)
CREATE TABLE speaking_topics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    level VARCHAR(20) DEFAULT 'BEGINNER' CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    time_limit INTEGER DEFAULT 120,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng User Course Progress (Tiến độ khóa học)
CREATE TABLE user_course_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    status VARCHAR(20) DEFAULT 'NOT_STARTED' CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')),
    completion_percentage DECIMAL(5,2) DEFAULT 0,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    UNIQUE(user_id, course_id)
);

-- Bảng User Lesson Progress (Tiến độ bài học)
CREATE TABLE user_lesson_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    status VARCHAR(20) DEFAULT 'LOCKED' CHECK (status IN ('LOCKED', 'UNLOCKED', 'IN_PROGRESS', 'COMPLETED')),
    score DECIMAL(5,2) DEFAULT 0,
    attempts INTEGER DEFAULT 0,
    completed_at TIMESTAMP,
    UNIQUE(user_id, lesson_id)
);

-- Bảng User Question Attempts (Lịch sử làm câu hỏi)
CREATE TABLE user_question_attempts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    question_id BIGINT REFERENCES questions(id) ON DELETE CASCADE,
    user_answer TEXT,
    is_correct BOOLEAN DEFAULT false,
    points_earned DECIMAL(3,1) DEFAULT 0,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng User Vocabulary Progress (Tiến độ từ vựng)
CREATE TABLE user_vocabulary_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    word_id BIGINT REFERENCES vocabulary_words(id) ON DELETE CASCADE,
    status VARCHAR(20) DEFAULT 'NEW' CHECK (status IN ('NEW', 'LEARNING', 'REVIEWING', 'MASTERED')),
    familiarity_level INTEGER DEFAULT 0,
    correct_count INTEGER DEFAULT 0,
    incorrect_count INTEGER DEFAULT 0,
    last_reviewed TIMESTAMP,
    next_review TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, word_id)
);

-- Bảng Vocabulary Review Sessions (Phiên ôn từ vựng)
CREATE TABLE vocabulary_review_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    session_date DATE NOT NULL,
    words_reviewed INTEGER DEFAULT 0,
    words_correct INTEGER DEFAULT 0,
    session_duration INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng User Speaking Attempts (Lần thử nói)
CREATE TABLE user_speaking_attempts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    topic_id BIGINT REFERENCES speaking_topics(id) ON DELETE CASCADE,
    audio_url VARCHAR(255),
    duration INTEGER,
    score DECIMAL(3,1),
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Writing Exercises (Bài tập viết)
CREATE TABLE writing_exercises (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    exercise_type VARCHAR(20) NOT NULL CHECK (exercise_type IN ('VI_TO_EN', 'EN_TO_VI')),
    level VARCHAR(20) DEFAULT 'BEGINNER' CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng User Writing Attempts (Bài làm của user)
CREATE TABLE user_writing_attempts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    exercise_id BIGINT REFERENCES writing_exercises(id) ON DELETE CASCADE,
    user_text TEXT NOT NULL,
    ai_score DECIMAL(4,1),
    ai_feedback TEXT,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng User Points (Điểm số)
CREATE TABLE user_points (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    total_points INTEGER DEFAULT 0,
    daily_points INTEGER DEFAULT 0,
    weekly_points INTEGER DEFAULT 0,
    last_reset_daily DATE,
    last_reset_weekly DATE
);

-- Bảng User Streaks (Chuỗi hoạt động)
CREATE TABLE user_streaks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    streak_type VARCHAR(50) NOT NULL,
    current_streak INTEGER DEFAULT 0,
    longest_streak INTEGER DEFAULT 0,
    last_activity_date DATE
);

-- Bảng User Daily Stats (Thống kê hàng ngày)
CREATE TABLE user_daily_stats (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    stat_date DATE NOT NULL,
    lessons_completed INTEGER DEFAULT 0,
    words_learned INTEGER DEFAULT 0,
    time_spent INTEGER DEFAULT 0,
    points_earned INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, stat_date)
);

-- ==================== THÊM DỮ LIỆU MẪU ====================

-- Insert Skills (6 kỹ năng chính)
INSERT INTO skills (name, description, has_sequential_unlock, order_index) VALUES 
('Grammar', 'English Grammar Lessons with Sequential Learning', true, 1),
('Vocabulary', 'Word Learning and Spaced Repetition Practice', false, 2),
('Listening', 'Audio Comprehension and Pronunciation', false, 3),
('Reading', 'Reading Comprehension and Speed Reading', false, 4),
('Speaking', 'Pronunciation Practice and Fluency Training', false, 5),
('Writing', 'Writing Skills and Translation Practice', false, 6);

-- Insert Vocabulary Topics
INSERT INTO vocabulary_topics (name, description, order_index) VALUES 
('Family', 'Family members and relationships', 1),
('Work', 'Workplace and career vocabulary', 2),
('Travel', 'Travel and transportation terms', 3),
('Food', 'Food and dining vocabulary', 4),
('TOEIC', 'TOEIC test vocabulary', 5),
('Daily Life', 'Common daily activities', 6),
('Technology', 'Technology and computer terms', 7),
('Health', 'Health and medical vocabulary', 8);

-- Insert sample vocabulary words
INSERT INTO vocabulary_words (word, pronunciation, part_of_speech, meaning_vietnamese, example_sentence, topic_id, difficulty) VALUES 
('family', '/ˈfæməli/', 'NOUN', 'gia đình', 'I love my family very much.', 1, 'EASY'),
('father', '/ˈfɑːðər/', 'NOUN', 'cha, bố', 'My father works in a bank.', 1, 'EASY'),
('mother', '/ˈmʌðər/', 'NOUN', 'mẹ', 'My mother is a teacher.', 1, 'EASY'),
('work', '/wɜːrk/', 'VERB', 'làm việc', 'I work from 9 to 5.', 2, 'EASY'),
('office', '/ˈɔːfɪs/', 'NOUN', 'văn phòng', 'Our office is in the city center.', 2, 'EASY'),
('travel', '/ˈtrævəl/', 'VERB', 'du lịch', 'I like to travel to new countries.', 3, 'MEDIUM'),
('airport', '/ˈeərpɔːrt/', 'NOUN', 'sân bay', 'The airport is very busy today.', 3, 'MEDIUM'),
('restaurant', '/ˈrestərɑːnt/', 'NOUN', 'nhà hàng', 'This restaurant serves delicious food.', 4, 'MEDIUM');

-- Insert sample courses
INSERT INTO courses (skill_id, name, description, level, order_index) VALUES 
(1, 'Basic Grammar', 'Learn fundamental English grammar rules', 'BEGINNER', 1),
(1, 'Intermediate Grammar', 'Advanced grammar structures and usage', 'INTERMEDIATE', 2),
(2, 'Essential Vocabulary', 'Core vocabulary for daily communication', 'BEGINNER', 1),
(2, 'Advanced Vocabulary', 'Academic and professional vocabulary', 'ADVANCED', 2),
(3, 'Basic Listening', 'Simple conversations and dialogues', 'BEGINNER', 1),
(4, 'Reading Comprehension', 'Understanding texts and articles', 'BEGINNER', 1);

-- Insert sample lessons for Basic Grammar course
INSERT INTO lessons (course_id, title, content, lesson_type, order_index, is_grammar_sequential) VALUES 
(1, 'Present Simple Tense', '<h2>Present Simple Tense</h2><p>The present simple tense is used for habits, facts, and general truths.</p><p><strong>Structure:</strong> Subject + Verb (base form) + Object</p><p><strong>Examples:</strong></p><ul><li>I work every day.</li><li>She speaks English.</li><li>They live in London.</li></ul>', 'THEORY', 1, true),
(1, 'Present Simple Practice', 'Practice exercises for present simple tense', 'PRACTICE', 2, true),
(1, 'Present Simple Test', 'Test your knowledge of present simple', 'TEST', 3, true),
(1, 'Present Continuous Tense', '<h2>Present Continuous Tense</h2><p>Used for actions happening now or temporary situations.</p><p><strong>Structure:</strong> Subject + am/is/are + Verb(-ing)</p><p><strong>Examples:</strong></p><ul><li>I am working now.</li><li>She is reading a book.</li><li>They are playing football.</li></ul>', 'THEORY', 4, true);

-- Insert sample questions
INSERT INTO questions (lesson_id, question_text, question_type, points, order_index, explanation) VALUES 
(2, 'Choose the correct form: "She _____ English every day."', 'multiple_choice', 1.0, 1, 'Present simple uses base form "speaks" for third person singular.'),
(2, 'Fill in the blank: "They _____ in New York." (live)', 'fill_blank', 1.0, 2, 'Present simple uses base form "live" for plural subjects.'),
(3, 'Choose the correct sentence:', 'multiple_choice', 1.0, 1, 'Present simple statement structure is Subject + Verb + Object.');

-- Insert question options
INSERT INTO question_options (question_id, option_text, is_correct, order_index) VALUES 
(1, 'speak', false, 1),
(1, 'speaks', true, 2),
(1, 'speaking', false, 3),
(1, 'spoken', false, 4),
(3, 'I am work every day', false, 1),
(3, 'I work every day', true, 2),
(3, 'I working every day', false, 3),
(3, 'I works every day', false, 4);

-- Insert sample writing exercises
INSERT INTO writing_exercises (title, content, exercise_type, level) VALUES 
('Translate: Family Introduction', 'Xin chào, tôi tên là Nam. Tôi 25 tuổi và tôi làm việc tại một công ty IT. Gia đình tôi có 4 người: bố, mẹ, em gái và tôi.', 'VI_TO_EN', 'BEGINNER'),
('Translate: Daily Routine', 'I wake up at 7 AM every morning. I brush my teeth, take a shower, and have breakfast. Then I go to work by bus.', 'EN_TO_VI', 'BEGINNER'),
('Translate: Work Description', 'Tôi làm việc trong một văn phòng hiện đại. Công việc của tôi là phát triển phần mềm. Tôi thường làm việc từ 8 giờ sáng đến 5 giờ chiều.', 'VI_TO_EN', 'INTERMEDIATE');

-- Insert sample listening passages
INSERT INTO listening_passages (title, content, audio_url, level) VALUES 
('Airport Announcement', 'Ladies and gentlemen, welcome to London Heathrow Airport. Flight BA123 to New York is now boarding at gate 15. Please have your boarding pass and passport ready.', 'https://example.com/audio1.mp3', 'BEGINNER'),
('Restaurant Order', 'Waiter: Good evening, what would you like to order? Customer: I''d like the grilled chicken with vegetables, please. Waiter: And what would you like to drink? Customer: A glass of orange juice, please.', 'https://example.com/audio2.mp3', 'BEGINNER');

-- Insert sample reading passages
INSERT INTO reading_passages (title, content, level, genre) VALUES 
('My Daily Routine', '<p>My name is Sarah and I am a student. Every morning, I wake up at 6:30 AM. I brush my teeth and take a shower. Then I have breakfast with my family. At 8:00 AM, I go to school by bus. I have classes until 3:00 PM. After school, I go home and do my homework. In the evening, I watch TV or read books. I go to bed at 10:00 PM.</p>', 'BEGINNER', 'Story'),
('The Benefits of Learning English', '<p>Learning English has many benefits in today''s world. First, English is the international language of business and technology. Many companies require employees who can speak English. Second, English helps you access more information on the internet. Most websites and online resources are in English. Finally, learning English opens up opportunities for travel and cultural exchange.</p>', 'INTERMEDIATE', 'Academic');

-- Insert sample speaking topics
INSERT INTO speaking_topics (title, description, level, time_limit) VALUES 
('Self Introduction', 'Introduce yourself including your name, age, occupation, and hobbies. Talk about your family and where you live.', 'BEGINNER', 60),
('Describe Your Daily Routine', 'Describe what you do from morning to evening. Include details about work, meals, and leisure activities.', 'BEGINNER', 90),
('Talk About Your Hometown', 'Describe your hometown including its location, population, attractions, and what you like about it.', 'INTERMEDIATE', 120),
('Discuss the Importance of Education', 'Give your opinion about education. Explain why education is important and how it can change people''s lives.', 'ADVANCED', 180);

-- Hiển thị thông báo hoàn thành
SELECT 'Database schema created successfully! Total tables: 23' AS status;