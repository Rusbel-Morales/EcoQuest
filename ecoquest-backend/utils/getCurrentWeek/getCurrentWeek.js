export function getCurrentWeek() {
	const today = new Date();
	const startOfYear = new Date(today.getFullYear(), 0, 1);
	const days = Math.floor((today - startOfYear) / (24 * 60 * 60 * 1000));
	return Math.ceil((days + startOfYear.getDay() + 1) / 7);
}