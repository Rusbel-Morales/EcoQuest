import moment from 'moment-timezone';
import missionStorage from '../data/MissionStorage.js';
import {
	getAssignedMissionForUser,
	markMissionAsComplete,
	getAssignedOptionalMissions,
	markMissionAsRerolled, assignNewOptionalMission, retrieveAndClearPendingPoints
} from '../services/missionService.js';
import {getCurrentAndLongestStreak} from "../services/statsService.js";
import {checkForAchievementCompletion} from "../utils/checkForAchievementCompletion/checkForAchievementCompletion.js";

export const getDailyMission = async (req, res) => {
	const { userId } = req.query;
	if (!userId) return res.status(404).json({ error: 'Missing userId parameter' });

	// Determine time range
	const currentTime = moment();
	const thresholdTime = moment.tz('America/Mexico_City').startOf('day').add(9, 'hours');
	let startTimestamp, endTimestamp;

	if (currentTime.isAfter(thresholdTime)) {
		startTimestamp = moment.tz('America/Mexico_City').startOf('day').add(9, 'hours').utc().format('YYYY-MM-DD HH:mm:ss');
		endTimestamp = moment.tz('America/Mexico_City').add(1, 'days').startOf('day').add(9, 'hours').utc().format('YYYY-MM-DD HH:mm:ss');
	} else {
		startTimestamp = moment.tz('America/Mexico_City').subtract(1, 'days').startOf('day').add(9, 'hours').utc().format('YYYY-MM-DD HH:mm:ss');
		endTimestamp = moment.tz('America/Mexico_City').startOf('day').add(9, 'hours').utc().format('YYYY-MM-DD HH:mm:ss');
	}

	try {
		const mission = await getAssignedMissionForUser(userId, startTimestamp, endTimestamp);
		if (!mission) return res.status(404).json({ error: 'No mission found' });

		const selectedMission = missionStorage.missions.find(mis => mis.id_mision === mission.id_mision);
		res.status(200).json({
			mission: selectedMission,
			isMissionCompleted: mission.is_completed,
		});
	} catch (error) {
		res.status(500).json({ error: 'Error fetching daily mission' });
	}
};

export const completeMission = async (req, res) => {
	const { userId, idMission } = req.body;

	if (!userId || !idMission) {
		return res.status(403).json({ error: 'Missing data' });
	}

	try {
		// Obtener la racha actual del usuario
		const { racha_actual: rachaActual } = await getCurrentAndLongestStreak(userId);

		const basePoints = 10; // Define los puntos base por misión
		const multiplier = 2;

		// Revisar si hay un bonus de puntos para el usuario por referidos
		const pendingPoints = await retrieveAndClearPendingPoints(userId);
		const points = basePoints + pendingPoints + rachaActual * multiplier;

		// Marcar la misión como completada
		const result = await markMissionAsComplete(userId, idMission, points);
		if (!result) {
			return res.status(404).json({ error: 'Mission not found or already completed' });
		}

		// Revisar si el usuario ha obtenido un logro
		await checkForAchievementCompletion(userId);

		res.status(200).json({
			message: 'Mission marked as completed',
			points
		});
	} catch (error) {
		console.error('Error completing mission:', error);
		res.status(500).json({ error: 'Error completing mission' });
	}
};

export const completeOptMission = async (req, res) => {
	const { userId, idMission } = req.body;

	if (!userId || !idMission) {
		return res.status(403).json({ error: 'Missing data' });
	}

	try {
		const baseOptMissionPoints = 10;
		// Revisar si hay un bonus de puntos para el usuario por referidos
		const pendingPoints = await retrieveAndClearPendingPoints(userId);

		// Marcar la misión como completada
		const result = await markMissionAsComplete(userId, idMission, baseOptMissionPoints + pendingPoints);

		if (!result) {
			return res.status(404).json({ error: 'Mission not found or already completed' });
		}

		// Revisar si el usuario ha obtenido un logro
		await checkForAchievementCompletion(userId);

		// Obtener y asignar una nueva misión para el usuario
		const newMissionId = await assignNewOptionalMission(userId);

		if (!newMissionId) {
			return res.status(404).json({ error: 'No optional missions found' });
		}

		// Obtener toda la información de la nueva misión
		const missionData = missionStorage.missions.find(mission => mission.id_mision === newMissionId);

		if (!missionData) {
			return res.status(404).json({ error: 'Assigned mission not found' });
		}

		// Responder con la nueva misión
		res.status(200).json({
			mission: missionData
		});
	} catch (error) {
		console.error('Error completing optional mission:', error);
		res.status(500).json({ error: 'Error completing optional mission' });
	}
}

export const fetchAllMissions = async (req, res) => {
	try {
		const missions = missionStorage.getAllMissions();
		res.status(200).json(missions);
	} catch (error) {
		res.status(500).json({ error: 'Error fetching missions' });
	}
};

export const getOptMissions = async (req, res) => {
	const { userId } = req.query;

	if (!userId) {
		return res.status(404).json({ error: 'Missing query data' });
	}

	try {
		const missions = await getAssignedOptionalMissions(userId, 3);

		if (!missions) {
			return res.status(404).json({ error: 'No missions found' });
		}

		res.status(200).json(missions)
	} catch (error) {
		console.error(error)
		res.status(500).json({ error: 'Error fetching optional missions' });
	}
}

export const rerollOptMission = async (req, res) => {
	const { userId, idMission } = req.body;

	if (!userId || !idMission) {
		return res.status(403).json({ error: 'Missing data' });
	}

	try {
		// Marcar misión como rerolled
		await markMissionAsRerolled(userId, idMission);

		// Assign new mission to user
		const newMissionId = await assignNewOptionalMission(userId);

		// Get full mission data
		const missionData = missionStorage.missions.find(mission => mission.id_mision === newMissionId);

		res.status(200).json({
			mission: missionData
		})
	} catch (error) {
		console.error(error)
		res.status(500).json({ error: 'Error rerolling optional mission' });
	}
}