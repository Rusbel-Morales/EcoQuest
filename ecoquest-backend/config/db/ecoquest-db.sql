-- TABLE: Users
CREATE TABLE usuario (
                         id_usuario VARCHAR(100) PRIMARY KEY NOT NULL,
                         nombre VARCHAR(100),
                         email VARCHAR(100) UNIQUE,
                         device_token VARCHAR(250) UNIQUE,
                         timestamp TIMESTAMP,
                         impacto_acumulado VARCHAR(255),
                         ref_code VARCHAR(255) UNIQUE NOT NULL,
                         puntos_pendientes INT DEFAULT 0,
                         id_invitador VARCHAR(100),
                         FOREIGN KEY (id_invitador) REFERENCES usuario(id_usuario)
);

-- TABLE: Missions
CREATE TABLE mision (
                        id_mision INT AUTO_INCREMENT PRIMARY KEY,
                        titulo VARCHAR(255),
                        descripcion TEXT,
                        categoria VARCHAR(255),
                        dificultad ENUM('facil', 'dificil')
);

-- TABLE: User Missions
CREATE TABLE usuario_mision (
                                id_usuario_mision INT AUTO_INCREMENT PRIMARY KEY,
                                timestamp_completada TIMESTAMP DEFAULT NULL,
                                timestamp_asignada TIMESTAMP DEFAULT NOW(),
                                puntos INT DEFAULT 0,
                                id_usuario VARCHAR(100) NOT NULL,
                                id_mision INT NOT NULL,
                                status ENUM('asignada', 'cancelada', 'completada') DEFAULT 'asignada' NOT NULL,
                                FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
                                FOREIGN KEY (id_mision) REFERENCES mision(id_mision) ON DELETE CASCADE
);

-- TABLE: Trophies
CREATE TABLE trofeo (
                        id_trofeo INT PRIMARY KEY,
                        tipo_trofeo ENUM('oro', 'plata', 'bronce'),
                        timestamp TIMESTAMP,
                        id_usuario VARCHAR(100) NOT NULL,
                        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- TABLE: Achievements
CREATE TABLE achievement (
                             id_achievement INT AUTO_INCREMENT PRIMARY KEY,
                             titulo VARCHAR(255),
                             descripcion TEXT
);

-- TABLE: User Achievements
CREATE TABLE usuario_achievement (
                                     id_usuario_achievement INT AUTO_INCREMENT PRIMARY KEY,
                                     timestamp TIMESTAMP,
                                     id_usuario VARCHAR(100) NOT NULL,
                                     id_achievement INT NOT NULL,
                                     FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
                                     FOREIGN KEY (id_achievement) REFERENCES achievement(id_achievement) ON DELETE CASCADE
);

-- TABLE: Weekly Trophies for seasonal leaderboards
CREATE TABLE weekly_trophies (
                                 id_trophy INT AUTO_INCREMENT PRIMARY KEY,
                                 id_usuario VARCHAR(100) NOT NULL,
                                 tipo_trofeo ENUM('oro', 'plata', 'bronce') NOT NULL,
                                 week INT,
                                 year INT,
                                 timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- VIEW: Completed Missions per Category
CREATE VIEW completed_missions_per_category AS
SELECT
    um.id_usuario,
    m.categoria,
    COUNT(um.id_mision) AS completed_missions_count
FROM
    usuario_mision um
        JOIN
    mision m ON um.id_mision = m.id_mision
WHERE
    um.timestamp_completada IS NOT NULL
GROUP BY
    um.id_usuario, m.categoria;

-- VIEW: Total Completed Missions by User
CREATE VIEW total_completed_missions AS
SELECT
    um.id_usuario,
    COUNT(um.id_mision) AS total_completed_missions
FROM
    usuario_mision um
WHERE
    um.status = 'completada'
GROUP BY
    um.id_usuario;

-- VIEW: Current Streak and Historical Streak
CREATE VIEW racha_usuario AS
WITH ranked_missions AS (
    SELECT
        id_usuario_mision,
        id_usuario,
        timestamp_completada,
        ROW_NUMBER() OVER (PARTITION BY id_usuario ORDER BY timestamp_completada) AS row_num,
        DATE(timestamp_completada) - INTERVAL ROW_NUMBER() OVER (PARTITION BY id_usuario ORDER BY timestamp_completada) DAY AS racha_group
        FROM
        usuario_mision
        WHERE
        status = 'completada'
        AND timestamp_completada IS NOT NULL
        ),
        racha_agrupada AS (
        SELECT
        id_usuario,
        racha_group,
        COUNT(*) AS racha_length,
        MAX(timestamp_completada) AS last_completion
        FROM
        ranked_missions
        GROUP BY
        id_usuario, racha_group
        ),
        racha_actual_calculada AS (
        SELECT
        id_usuario,
        racha_length,
        last_completion
        FROM
        racha_agrupada
        WHERE
        last_completion >= CURDATE() - INTERVAL 1 DAY
        )
SELECT
    ra.id_usuario,
    COALESCE((SELECT rac.racha_length
              FROM racha_actual_calculada rac
              WHERE rac.id_usuario = ra.id_usuario
              ORDER BY rac.last_completion DESC
             LIMIT 1), 0) AS racha_actual,
    MAX(ra.racha_length) AS racha_maxima
FROM
    racha_agrupada ra
        LEFT JOIN
    racha_actual_calculada rac ON ra.id_usuario = rac.id_usuario
GROUP BY
    ra.id_usuario;

-- VIEW: Weekly Leaderboard for seasonal competitions
CREATE VIEW weekly_leaderboard AS
SELECT
    u.id_usuario,
    u.nombre,
    SUM(um.puntos) AS total_puntos
FROM
    usuario u
        JOIN
    usuario_mision um ON u.id_usuario = um.id_usuario
WHERE
    um.status = 'completada'
        AND WEEK(um.timestamp_completada) = WEEK(CURDATE())
        AND YEAR(um.timestamp_completada) = YEAR(CURDATE())
        GROUP BY
        u.id_usuario, u.nombre
        ORDER BY
        total_puntos DESC;

-- VIEW: User Stats Summary
CREATE VIEW user_stats AS
SELECT
    u.id_usuario,
    COALESCE(ru.racha_actual, 0) AS racha_actual,
    COALESCE(ru.racha_maxima, 0) AS racha_maxima,
    COALESCE(SUM(um.puntos), 0) AS total_puntos,
    COUNT(um.id_usuario_mision) AS total_misiones_completadas
FROM
    usuario u
        LEFT JOIN
    racha_usuario ru ON u.id_usuario = ru.id_usuario
        LEFT JOIN
    usuario_mision um ON u.id_usuario = um.id_usuario AND um.status = 'completada'
GROUP BY
    u.id_usuario;

-- VIEW: Points per Day in CST timezone
CREATE VIEW puntos_por_dia AS
SELECT
    usuarios.id_usuario,
    dias.dia,
    COALESCE(SUM(um.puntos), 0) AS puntos_totales
FROM
    (SELECT DATE(CONVERT_TZ(CURDATE(), '+00:00', '-06:00')) - INTERVAL n DAY AS dia
     FROM (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS numeros) AS dias
CROSS JOIN
    (SELECT DISTINCT id_usuario FROM usuario_mision) AS usuarios
LEFT JOIN
    usuario_mision um ON usuarios.id_usuario = um.id_usuario
        AND DATE(CONVERT_TZ(um.timestamp_completada, '+00:00', '-06:00')) = dias.dia
        AND um.status = 'completada'
WHERE
    dias.dia BETWEEN DATE(CONVERT_TZ(CURDATE(), '+00:00', '-06:00')) - INTERVAL 7 DAY AND DATE(CONVERT_TZ(CURDATE(), '+00:00', '-06:00'))
GROUP BY
    usuarios.id_usuario, dias.dia
ORDER BY
    usuarios.id_usuario, dias.dia;

-- VIEW: Count Trophies by User
CREATE VIEW count_trophies_user AS
SELECT
    u.id_usuario,
    COALESCE(SUM(CASE WHEN wt.tipo_trofeo = 'oro' THEN 1 ELSE 0 END), 0) AS total_oro,
    COALESCE(SUM(CASE WHEN wt.tipo_trofeo = 'plata' THEN 1 ELSE 0 END), 0) AS total_plata,
    COALESCE(SUM(CASE WHEN wt.tipo_trofeo = 'bronce' THEN 1 ELSE 0 END), 0) AS total_bronce
FROM
    usuario u
        LEFT JOIN
    weekly_trophies wt ON u.id_usuario = wt.id_usuario
GROUP BY
    u.id_usuario;