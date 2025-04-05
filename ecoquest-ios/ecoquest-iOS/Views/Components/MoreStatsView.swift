//
//  EstadisticasView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/17/24.
//

import SwiftUI

struct MoreStatsView: View {
    @EnvironmentObject var authViewModel: AuthViewModel
    @StateObject private var viewModel: EstadisticasSectionViewModel = EstadisticasSectionViewModel()
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Más estadísticas")
                .font(.title2)
                .fontWeight(.bold)
                .padding(.bottom, 8)
            
            // Statistic rows with icons
            StatisticRow(iconName: "leaf.fill", title: "EcoXP Total", value: Int(viewModel.userStats?.totalPuntos ?? "0") ?? 0)
            StatisticRow(iconName: "checkmark.seal.fill", title: "Total de misiones completadas", value: viewModel.userStats?.totalMisionesCompletadas ?? 0)
            StatisticRow(iconName: "flame.fill", title: "Racha más larga", value: viewModel.userStats?.rachaMaxima ?? 0)
            StatisticRow(iconName: "calendar.circle.fill", title: "Racha actual", value: viewModel.userStats?.rachaActual ?? 0)
        }
        .padding(.horizontal)
        .onAppear {
            viewModel.fetchStatsData(userId: authViewModel.userId)
        }
    }
}

struct StatisticRow: View {
    var iconName: String
    var title: String
    var value: Int
    
    var body: some View {
        HStack(spacing: 10) {
            Image(systemName: iconName)
                .foregroundColor(.green)
                .font(.body)
            
            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.subheadline)
                    .fontWeight(.medium)
                    .foregroundColor(.primary)
                
                Text("\(value)")
                    .font(.subheadline)
                    .fontWeight(.semibold)
                    .foregroundColor(.secondary)
            }
            Spacer()
        }
        .padding(.vertical, 2)
    }
}

