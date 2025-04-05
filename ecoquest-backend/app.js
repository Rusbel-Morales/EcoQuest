import express from 'express';
import cors from 'cors';
import corsOptions from './config/corsOptions.js';
import scheduleAssignDailyMission from './utils/scheduleAssignDailyMission/scheduleAssignDailyMission.js';
import missionStorage from './data/MissionStorage.js';
import missionRoutes from './routes/missionRoutes.js';
import authRoutes from './routes/authRoutes.js';
import statsRoutes from './routes/statsRoutes.js';
import { getAllMissions } from './services/missionService.js';
import {scheduleAwardTrophies} from "./utils/scheduleAwardTrophies/scheduleAwardTrophies.js";

// Initialize express app
const app = express();

// Middleware
app.use(cors(corsOptions));
app.use(express.json());

// Schedule daily missions
scheduleAssignDailyMission('1 8 * * *');
scheduleAwardTrophies('35 19 * * *');

// Load initial missions into storage
const missions = await getAllMissions();
missionStorage.setMissions(missions);

// Routes
app.use('/missions', missionRoutes);
app.use('/auth', authRoutes);
app.use('/stats', statsRoutes);

export default app;