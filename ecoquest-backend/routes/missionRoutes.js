import express from 'express';
import {
	getDailyMission,
	completeMission,
	fetchAllMissions,
	getOptMissions,
	rerollOptMission, completeOptMission
} from '../controllers/missionController.js';

const router = express.Router();

router.get('/get-daily-mission', getDailyMission);
router.post('/complete-mission', completeMission);
router.get('/fetch-all-missions', fetchAllMissions);
router.get('/get-opt-missions', getOptMissions);
router.post('/reroll-opt-mission', rerollOptMission);
router.post('/complete-opt-mission', completeOptMission);

export default router;