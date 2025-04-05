import connectWithConnector from "../../config/db/dbConnection.js";

export const checkForAchievementCompletion = async (userId) => {
	try {
		const { total_misiones_completadas: nMissionsCompleted } = await queryMissionsCompleted(userId);

		const achievementId = getAchievementId(nMissionsCompleted);

		if (!achievementId) return; // No achievement to add, exit early

		await insertUserAchievement(userId, achievementId);
	} catch (error) {
		console.error(`Error checking for achievement completion: ${error.message}`);
		throw error;
	}
};

const queryMissionsCompleted = async (userId) => {
	const pool = await connectWithConnector();
	const query = `SELECT total_misiones_completadas FROM user_stats WHERE id_usuario = ?`;

	try {
		const [rows] = await pool.query(query, [userId]);
		return rows[0];
	} catch (error) {
		console.error(`Error querying missions completed: ${error.message}`);
		throw error;
	} finally {
		await pool.end();
	}
};

const insertUserAchievement = async (userId, achievementId) => {
	const pool = await connectWithConnector();
	const query = `INSERT INTO usuario_achievement (id_usuario, id_achievement) VALUES (?, ?)`;

	try {
		await pool.query(query, [userId, achievementId]);
	} catch (error) {
		console.error(`Error inserting user achievement: ${error.message}`);
		throw error;
	} finally {
		await pool.end();
	}
};

const getAchievementId = (nMissionsCompleted) => {
	switch (nMissionsCompleted) {
		case 1:
			return 1;
		case 5:
			return 2;
		case 10:
			return 3;
		case 20:
			return 4;
		default:
			return null; // No achievement for this count
	}
};