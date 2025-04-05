import cron from 'node-cron';
import connectWithConnector from "../../config/db/dbConnection.js";
import {queryTopLeaderboard} from "../../services/statsService.js";
import {getCurrentWeek} from "../getCurrentWeek/getCurrentWeek.js";

export const scheduleAwardTrophies = async (timeExpression) => {
	const TROPHY_TYPES = ['oro', 'plata', 'bronce']

	cron.schedule(timeExpression, async () => {
		try {
			const pool = await connectWithConnector();

			// Query the top 3 leaderboard
			const topLeaderboard = await queryTopLeaderboard();

			// Define the query to insert into the weekly_leaderboard table
			const query = `INSERT INTO weekly_trophies (id_usuario, tipo_trofeo, week, year) VALUES (?, ?, ?, ?)`;

			const currentWeek = getCurrentWeek();
			const currentYear = new Date().getFullYear();

			// Award trophies to the top 3 users
			for (let i = 0; i < topLeaderboard.length; i++) {
				const userId = topLeaderboard[i].id_usuario;
				const trophyType = TROPHY_TYPES[i];
				await pool.execute(query, [userId, trophyType, currentWeek, currentYear]);
			}

			// Clean up the pool connection
			await pool.end();
		} catch (error) {
			console.error('Error running scheduleAwardTrophies:', error);
		}
	})
}