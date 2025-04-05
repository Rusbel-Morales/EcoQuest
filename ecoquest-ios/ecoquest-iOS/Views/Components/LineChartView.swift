import SwiftUI
import Charts

// Estructura de los datos para la gráfica
struct LineChartData {
    let week: String
    let points: Int
}

// Vista de la gráfica de líneas con puntos
struct LineChartView: View {
    let chartData: [DailyPoints]
   
    var body: some View {
        VStack(alignment: .leading) {
            Text("Progreso Semanal")
                .font(.title2)
                .fontWeight(.bold)
                .padding(.bottom, 8)

            Chart {
                ForEach(chartData, id: \.dia) { data in
                    let dayWeek = dayOfWeek(dateString: data.dia)
                    LineMark(
                        x: .value("Día", dayWeek ?? ""),
                        y: .value("Puntos", data.puntos_totales)
                    )
                    .lineStyle(StrokeStyle(lineWidth: 2))
                    .foregroundStyle(Color.green)
                    
                    PointMark(
                        x: .value("Día", dayWeek ?? ""),
                        y: .value("Puntos", data.puntos_totales)
                    )
                    .foregroundStyle(Color.orange)
                    .symbolSize(20) // Adjust symbol size for visibility
                }
            }
            .chartXAxis {
                AxisMarks(values: .automatic) { _ in
                    AxisGridLine().foregroundStyle(.gray.opacity(0.3))
                    AxisTick().foregroundStyle(.gray.opacity(0.6))
                    AxisValueLabel()
                        .font(.caption)
                        .foregroundStyle(Color(.secondaryLabel))
                }
            }
            .chartYAxis {
                AxisMarks(values: .automatic) { _ in
                    AxisGridLine().foregroundStyle(.gray.opacity(0.3))
                    AxisTick().foregroundStyle(.gray.opacity(0.6))
                    AxisValueLabel()
                        .font(.caption)
                        .foregroundStyle(Color(.secondaryLabel))
                }
            }
            .chartXAxisLabel("Días", position: .bottom)
            .chartYAxisLabel("Puntos", position: .leading)
            .frame(height: 230)
            .padding(.horizontal, 16)
            .padding(.vertical, 8)
            .background(Color(UIColor.systemBackground).cornerRadius(12).shadow(radius: 4))
        }
        .padding()
    }
}

#Preview {
    // Sample data for preview
    let sampleData = [
        DailyPoints(dia: "30-10-2024", puntos_totales: 0),
        DailyPoints(dia: "31-10-2024", puntos_totales: 0),
        DailyPoints(dia: "01-11-2024", puntos_totales: 0),
        DailyPoints(dia: "02-11-2024", puntos_totales: 0),
        DailyPoints(dia: "03-11-2024", puntos_totales: 0),
        DailyPoints(dia: "04-11-2024", puntos_totales: 350),
        DailyPoints(dia: "05-11-2024", puntos_totales: 210)
    ]
   
    LineChartView(chartData: sampleData)
}
