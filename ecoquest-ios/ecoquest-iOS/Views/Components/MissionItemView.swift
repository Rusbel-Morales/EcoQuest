//
//  MissionItemView.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/14/24.
//

import SwiftUI

struct MissionItem: View {
    @Binding var path: NavigationPath
    @ObservedObject var viewModel: MissionItemViewModel
    @State private var isMissionCompletedModalPresented = false
    @State private var isConfirmationDialogPresented = false
    @Binding var selectedMission: Mission?
    @Binding var completedMission: Mission?
    @Binding var isShareSheetPresented: Bool
    
    let missionTitle: String
    let missionDescription: String
    
    var body: some View {
        HStack(spacing: 12) {
            Button(action: {
                guard !viewModel.isChecked else { return }
                viewModel.toggleMissionCompletion()
                isConfirmationDialogPresented = true
            }) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(viewModel.isChecked ? Color.green : Color.gray.opacity(0.3))
                    .frame(width: 30, height: 30)
                    .overlay(
                        Image(systemName: "checkmark")
                            .foregroundColor(.white)
                            .opacity(viewModel.isChecked ? 1 : 0)
                    )
                    .shadow(radius: 1, x: 0, y: 1)
            }
            .buttonStyle(PlainButtonStyle())
            .confirmationDialog("Complete Mission", isPresented: $isConfirmationDialogPresented, titleVisibility: .visible) {
                Button("Confirm") {
                    viewModel.toggleMissionCompletion()
                    completedMission = viewModel.misionesViewModel.dailyMission
                    isMissionCompletedModalPresented = true
                }
                Button("Cancel", role: .cancel) {}
            }
            
            Button(action: {
                viewModel.presentDescriptionModal()
                selectedMission = viewModel.misionesViewModel.dailyMission
            }) {
                Text(missionTitle)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(viewModel.isChecked ? .gray : .primary)
                    .strikethrough(viewModel.isChecked, color: .red)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
        .padding(.vertical, 8)
        .padding(.horizontal, 12)
        .background(Color.white.opacity(0.9))
        .cornerRadius(10)
        .shadow(color: Color.black.opacity(0.1), radius: 2, x: 0, y: 1)
        .fullScreenCover(isPresented: $isMissionCompletedModalPresented) {
            MissionCompleteView(path: $path, isPresented: $isMissionCompletedModalPresented, isShareSheetPresented: $isShareSheetPresented, completedMission: $completedMission)
        }
    }
}
