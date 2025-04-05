//
//  LogrosView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/17/24.
//

import SwiftUI

struct LogrosView: View {
    @EnvironmentObject var authViewModel: AuthViewModel
    @StateObject private var viewModel = LogrosViewModel()
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Logros")
                .font(.title2)
                .fontWeight(.bold)
                .padding(.bottom, 8)
            
            if viewModel.isLoading {
                ProgressView()
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 8)
            } else {
                VStack(spacing: 15) {
                    HStack(spacing: 15) {
                        AchievementCard(
                            title: "EcoPrincipiante",
                            description: "Completa tu primera misión en EcoQuest.",
                            imageName: "leaf.fill",
                            isCompleted: viewModel.achievements.first(where: { $0.idAchievement == 1 })?.isCompleted ?? false
                        )
                        AchievementCard(
                            title: "EcoExplorador",
                            description: "Completa un total de 5 misiones.",
                            imageName: "globe.europe.africa.fill",
                            isCompleted: viewModel.achievements.first(where: { $0.idAchievement == 2 })?.isCompleted ?? false
                        )
                    }
                    
                    HStack(spacing: 15) {
                        AchievementCard(
                            title: "EcoHéroe",
                            description: "Completa un total de 10 misiones.",
                            imageName: "star.circle.fill",
                            isCompleted: viewModel.achievements.first(where: { $0.idAchievement == 3 })?.isCompleted ?? false
                        )
                        AchievementCard(
                            title: "EcoMaestro",
                            description: "Completa un total de 20 misiones.",
                            imageName: "crown.fill",
                            isCompleted: viewModel.achievements.first(where: { $0.idAchievement == 4 })?.isCompleted ?? false
                        )
                    }
                }
                .padding(.horizontal, 10)
            }
        }
        .padding()
        .background(Color(red: 246/255, green: 233/255, blue: 107/255))
        .cornerRadius(12)
        .shadow(radius: 2)
        .onAppear {
            viewModel.fetchAchievements(userId: authViewModel.userId)
        }
    }
}

struct AchievementCard: View {
    var title: String
    var description: String
    var imageName: String
    var isCompleted: Bool
    
    var body: some View {
        VStack(spacing: 6) {
            Image(systemName: imageName)
                .resizable()
                .scaledToFit()
                .frame(width: 50, height: 50)
                .foregroundColor(isCompleted ? .black : Color(red: 120/255, green: 120/255, blue: 120/255))
            
            Text(title)
                .font(.system(size: 13, weight: .semibold))
                .foregroundColor(isCompleted ? .primary : Color(red: 80/255, green: 80/255, blue: 80/255))
            
            Text(description)
                .font(.footnote)
                .foregroundColor(isCompleted ? .secondary : Color(red: 100/255, green: 100/255, blue: 100/255))
                .multilineTextAlignment(.center)
                .frame(height: 40)
        }
        .padding(8)
        .frame(width: 130, height: 140)
        .background(isCompleted ? Color.green.opacity(0.2) : Color(red: 230/255, green: 230/255, blue: 230/255))
        .cornerRadius(10)
        .shadow(color: isCompleted ? Color.green.opacity(0.3) : .clear, radius: 4)
    }
}

#Preview {
    LogrosView()
}
