import {
	queryAchievements,
	queryLeaderboard, queryTrophyCount,
	queryUserStats, queryXpBarData
} from '../services/statsService.js';
import { selectGraphData } from '../services/missionService.js';
import connectWithConnector from "../config/db/dbConnection.js";

export const getUserStats = async (req, res) => {
	const { userId } = req.query;

	if (!userId) {
		return res.status(400).json({ error: 'El id_usuario es requerido.' });
	}

	try {
		// Fetch the total number of completed missions for the user
		const { racha_actual: rachaActual, racha_maxima: rachaMaxima, total_puntos: totalPuntos, total_misiones_completadas: totalMisionesCompletadas } = await queryUserStats(userId);

		// Send the response
		console.log(rachaActual, rachaMaxima, totalPuntos, totalMisionesCompletadas)

		res.status(200).json({
			userId,
			rachaActual,
			rachaMaxima,
			totalPuntos,
			totalMisionesCompletadas
		});
	} catch (error) {
		console.error('Error fetching completed user stats', error);
		res.status(500).json({ error: 'Error al obtener las estadisticas del usuario' });
	}
};

export const getLeaderboard = async (req, res) => {
	try {
		const leaderboard = await queryLeaderboard()

		res.status(200).json({
			leaderboard
		})
	} catch (error) {
		res.status(500).json({ error: 'Error fetching leaderboard' });
	}
}

export const getXpBarData = async (req, res) => {
	try {
		const { userId } = req.query;

		if (!userId) {
			return res.status(400).json({ error: 'Missing userId parameter' });
		}

		const { mis_puntos, mi_posicion, puntos_siguiente_usuario } = await queryXpBarData(userId);

		// Parse the results
		const misPuntos = parseInt(mis_puntos);
		const miPosicion = parseInt(mi_posicion);
		const puntosSiguienteUsuario = parseInt(puntos_siguiente_usuario);

		res.status(200).json({
			miPosicion,
			misPuntos,
			puntosSiguienteUsuario
		});

	} catch (error) {
		res.status(500).json({ error: 'Error fetching xp bar data' });
	}
}


// Nueva función auxiliar para transformar el formato de fecha
const parseTimestampToDate = (timestamp) => {
	const date = new Date(timestamp);
	const day = String(date.getDate()).padStart(2, '0');
	const month = String(date.getMonth() + 1).padStart(2, '0'); // Meses en JavaScript empiezan en 0
	const year = date.getFullYear();
	return `${day}-${month}-${year}`;
};

// Actualizar la función `getPointsByDate`


export const getPointsByDate = async (req, res) => {
	const { userId } = req.query;

	if (!userId) {
		return res.status(400).json({ error: 'Missing userId' });
	}

	try {
		const rows = await selectGraphData(userId);

		if (rows.length === 0) {
			return res.status(404).json({ error: 'No data found for the given user' });
		}

		// Transformar el formato de fecha en cada fila
		const formattedRows = rows.map(row => ({
			dia: parseTimestampToDate(row.dia),
			puntos_totales: parseInt(row.puntos_totales, 10)
		}));

		res.status(200).json(formattedRows);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error: 'Error fetching points per day' });
	}
};

export const getUserAchievements = async (req, res) => {
	const {userId} = req.query;

	if (!userId) {
		return res.status(400).json({error: 'El userId es requerido.'});
	}

	try {
		const achievements = await queryAchievements(userId);

		// Send the response
		res.status(200).json({
			achievements
		});
	} catch (error) {
		console.error(error)
		res.status(500).json({ error: 'Error fetching user achievements' });
	}
}

export const getUserTrophies = async (req, res) => {
	const {userId} = req.query;

	if (!userId) {
		return res.status(400).json({error: 'El userId es requerido.'});
	}

	try {
		const trophyCount = await queryTrophyCount(userId);

		// Send the response
		res.status(200).json({
			trophyCount
		});
	} catch (error) {
		console.error(error)
		res.status(500).json({ error: 'Error fetching user trophies' });
	}
}