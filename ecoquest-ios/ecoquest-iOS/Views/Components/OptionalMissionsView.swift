//
//  OptionalMissionsView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/14/24.
//

import SwiftUI

struct OptionalMissionsView: View {
    @StateObject private var viewModel = OptionalMissionsViewModel()
    @EnvironmentObject var authViewModel: AuthViewModel
    @Binding var path: NavigationPath
    @Binding var selectedMission: Mission?
    @Binding var completedMission: Mission?
    @Binding var isShareSheetPresented: Bool
    
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            // Title Section
            HStack(spacing: 8) {
                Image("brujula")
                    .resizable()
                    .frame(width: 28, height: 28)
                    .padding(4)
                
                Text("Misiones adicionales")
                    .font(.title2)
                    .bold()
                    .foregroundColor(.primary)
            }
            .padding(.bottom, 8)
            
            // Main Content Section
            if viewModel.isLoading {
                ProgressView()
                    .scaleEffect(1.2)
                    .padding(.vertical, 20)
                    .frame(maxWidth: .infinity)
            } else if viewModel.missions.isEmpty {
                Text("No hay misiones opcionales disponibles.")
                    .font(.subheadline)
                    .foregroundColor(.gray)
                    .padding(.vertical, 15)
                    .frame(maxWidth: .infinity)
            } else {
                // List of Available Missions
                VStack(spacing: 12) {
                    ForEach(viewModel.missions.indices, id: \.self) { index in
                        if let mission = viewModel.missions[index] {
                            if viewModel.isRerolling[index] ?? false {
                                HStack(spacing: 6) {
                                    ProgressView()
                                        .frame(width: 18, height: 18)
                                    Text("Rerolando misión...")
                                        .font(.footnote)
                                        .foregroundColor(.gray)
                                }
                                .padding(.vertical, 6)
                            } else {
                                MissionRow(mission: mission, index: index, viewModel: viewModel, path: $path, selectedMission: $selectedMission, isShareSheetPresented: $isShareSheetPresented, completedMission: $completedMission)
                            }
                        } else {
                            Text("Misión no disponible.")
                                .font(.footnote)
                                .foregroundColor(.gray)
                                .padding(.vertical, 6)
                        }
                    }
                }
            }
        }
        .padding(.horizontal, 20)
        .padding(.top, 12)
        .onAppear {
            viewModel.fetchOptMissions(userId: authViewModel.userId)
        }
    }
}
