import express from 'express';
import {
	getLeaderboard,
	getPointsByDate,
	getUserAchievements,
	getUserStats, getUserTrophies,
	getXpBarData
} from '../controllers/statsController.js';

const router = express.Router();

// Route to get the total number of completed missions for a specific user
router.get('/get-user-stats', getUserStats);
router.get('/get-leaderboard', getLeaderboard);
router.get('/get-xp-bar-data', getXpBarData);
router.get('/get-points-day', getPointsByDate);
router.get('/get-user-achievements', getUserAchievements);
router.get('/get-user-trophies', getUserTrophies)

export default router;