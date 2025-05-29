-- Users
INSERT INTO "USER_" (id, login, registration_date, email, password)
VALUES (1000, 'basia123', CURRENT_TIMESTAMP, 'basia@gmail.com', 'password123'),
       (1001, 'franek456', CURRENT_TIMESTAMP, 'franek@gmail.com',
        'password456');
-- Bots
INSERT INTO bot (id, login, registration_date, owner_id, token)
VALUES (2000, 'bot123', CURRENT_TIMESTAMP, 1001, 'abc123token456'),
       (2001, 'bot456', CURRENT_TIMESTAMP, 1001, 'xyz789token321');

-- Bot features
INSERT INTO bot_features (bot_id, features)
VALUES (2000, 'MODERATION'),
       (2000, 'HELP_COMMANDS'),
       (2001, 'MUSIC_PLAYBACK'),
       (2001, 'PLAYLIST_MANAGEMENT');

-- Servers
INSERT INTO SERVER (id, name, owner_id)
VALUES (3000, 'server #1', 1001),
       (3001, 'server #2', 1001);
-- Roles

INSERT INTO role (id, server_id, name, access_level)
VALUES (4000, 3000, 'Admin', 100),
       (4001, 3000, 'Moderator', 50),
       (4002, 3000, 'Member', 10);

-- Memberships
INSERT INTO membership (id, member_id, server_id, role_id, join_date, leave_date)
VALUES (5000, 1000, 3000, 4002, CURRENT_TIMESTAMP - 20, NULL),
       (5001, 1001, 3000, 4000, CURRENT_TIMESTAMP - 30, NULL);

-- Text Channels
INSERT INTO channel (id, server_id, name, required_access_level)
VALUES (6000, 3000, 'general', 0),
       (6001, 3000, 'off-topic', 0),
       (6002, 3000, 'admin-chat', 100);

INSERT INTO text_channel (id, slowmode)
VALUES (6000, 10),
       (6001, 5),
       (6002, 0);

-- Voice Channels
INSERT INTO channel (id, server_id, name, required_access_level)
VALUES (7000, 3000, 'vc #1', 0),
       (7001, 3000, 'vc #2', 0),
       (7002, 3000, 'vc #3', 0);

INSERT INTO voice_channel (id, bitrate, max_users)
VALUES (7000, 64, 10),
       (7001, 96, 5),
       (7002, 64, 20);

-- Messages
INSERT INTO message (id, contents, author_id, channel_id, created_at, status)
VALUES (8000, 'Hello!', 1001, 6000, CURRENT_TIMESTAMP - 10, 'APPROVED'),
       (8001, 'Hi!', 1000, 6000, CURRENT_TIMESTAMP - 9,'APPROVED'),
       (8002, 'I love cats!', 1000, 6001, CURRENT_TIMESTAMP - 8,'APPROVED');
-- Emojis
INSERT INTO emoji (id, server_id, name, image_data)
VALUES (9000, 3000, 'smiley', X'01020304'),
       (9001, 3000, 'heart', X'05060708'),
       (9002, 3000, 'thumbsup', X'090A0B0C');

-- Reactions
INSERT INTO reaction (id, emoji_id, message_id, user_id, created_at)
VALUES (10000, 9000, 8000, 1001, CURRENT_TIMESTAMP - 5),
       (10001, 9001, 8000, 1000, CURRENT_TIMESTAMP - 4),
       (10002, 9001, 8000, 1001, CURRENT_TIMESTAMP - 3);

-- Server Events
INSERT INTO server_event (id, name, description, server_id, start_time, end_time, status)
VALUES (11000, 'event #1', 'first event', 3000,
        CURRENT_TIMESTAMP + 86400, CURRENT_TIMESTAMP + 172800, 'SCHEDULED'),
       (11001, 'event #2', 'second event', 3000,
        CURRENT_TIMESTAMP + 43200, CURRENT_TIMESTAMP + 57600, 'SCHEDULED'),
       (11002, 'event #3', 'third event', 3001,
        CURRENT_TIMESTAMP + 259200, CURRENT_TIMESTAMP + 345600, 'SCHEDULED');

-- Participations
INSERT INTO participation (id, participant_id, event_id, sign_date)
VALUES (12000, 5000, 11000, CURRENT_TIMESTAMP - 2),
       (12001, 5000, 11001, CURRENT_TIMESTAMP - 1),
       (12002, 5001, 11002, CURRENT_TIMESTAMP);

-- Subscriptions
INSERT INTO subscription (id, user_id, level, start_date, end_date)
VALUES (13000, 1001, 'PRO', CURRENT_TIMESTAMP - 60, CURRENT_TIMESTAMP + 30),
       (13001, 1000, 'CLASSIC', CURRENT_TIMESTAMP - 30, CURRENT_TIMESTAMP + 60);