//
//  EstadisticasView.swift
//  ecoquest-iOS
//
//  Created by Administrador on 21/10/24.
//

import SwiftUI
import Charts

struct EstadisticasView: View {
    @Binding var path: NavigationPath
    @StateObject var viewModel: PointsByDateViewModel = PointsByDateViewModel()
    @EnvironmentObject var authViewModel: AuthViewModel
    let rachaActual: Int = 0

    var body: some View {
        VStack(spacing: 20) {
            // Título
            VStack {
                Text("Estadísticas")
                    .font(.title)
                    .bold()
            }
            .padding()
            .frame(maxWidth: .infinity, maxHeight: 120, alignment: .bottom)
            .background(Color(red: 181/255, green: 222/255, blue: 135/255))
            .cornerRadius(10)
            
            VStack {               
                if viewModel.isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity)
                        .padding()
                } else {
                    // Gráfica de líneas con puntos
                    LineChartView(chartData: viewModel.dailyPoints)
                }
            }
            .frame(width: 350, height: .infinity)
            .padding()
            .background(Color(red: 246/255, green: 233/255, blue: 107/255))
            .cornerRadius(10)
            
            MoreStatsView()

            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(red: 245/255, green: 245/255, blue: 245/255))
        .edgesIgnoringSafeArea(.all)
        .onAppear {
            viewModel.fetchPointsByDate(userId: authViewModel.userId)
        }
    }
}

#Preview {
    // Datos de prueba
    let testDailyPoints = [
        DailyPoints(dia: "2024-10-01", puntos_totales: 5)
    ]
   
    let viewModel = PointsByDateViewModel(testData: testDailyPoints)
   
    EstadisticasView(
        path: .constant(NavigationPath()),
        viewModel: viewModel
    )
}
