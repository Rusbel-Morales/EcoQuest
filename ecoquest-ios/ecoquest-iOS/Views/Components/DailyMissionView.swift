//
//  DailyMissionView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/14/24.
//

import SwiftUI

struct DailyMissionView: View {
    @Binding var path: NavigationPath
    @ObservedObject var viewModel: MisionesViewModel
    @ObservedObject var signInViewModel: AuthViewModel
    @Binding var selectedMission: Mission?
    @Binding var completedMission: Mission?
    @Binding var isShareSheetPresented: Bool
    
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            HStack(spacing: 8) {
                Image("objetivo")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 28, height: 28)
                
                Text("Misión del día")
                    .font(.title2)
                    .bold()
                    .foregroundColor(.primary)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.bottom, 8)
            
            if viewModel.isFetching {
                HStack(spacing: 8) {
                    ProgressView()
                        .scaleEffect(1.2)
                    Text("Cargando misión...")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 10)
            } else if let mission = viewModel.dailyMission {
                MissionItem(
                    path: $path,
                    viewModel: MissionItemViewModel(
                        missionId: mission.id_mision,
                        userId: signInViewModel.userId,
                        misionesViewModel: viewModel
                    ),
                    selectedMission: $selectedMission,
                    completedMission: $completedMission,
                    isShareSheetPresented: $isShareSheetPresented,
                    missionTitle: mission.titulo,
                    missionDescription: mission.descripcion
                )
                .padding(.top, 8)
            } else {
                Text("No hay misión disponible en este momento.")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .padding(.vertical, 12)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
        .padding(16)
        .background(Color(red: 246/255, green: 233/255, blue: 107/255))
        .cornerRadius(12)
        .shadow(radius: 4)
        .padding(.horizontal)
    }
}
