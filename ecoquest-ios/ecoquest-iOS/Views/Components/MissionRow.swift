import SwiftUI

struct MissionRow: View {
    let mission: Mission
    let index: Int
    @ObservedObject var viewModel: OptionalMissionsViewModel
    @EnvironmentObject var authViewModel: AuthViewModel
    @State private var isMissionCompletedModalPresented = false
    @State private var isCompleteConfirmationDialogPresented = false
    @State private var isMissionDescriptionModalPresented = false
    @State private var isRerollConfirmationDialogPresented = false
    @Binding var path: NavigationPath
    @Binding var selectedMission: Mission?
    @Binding var isShareSheetPresented: Bool
    @Binding var completedMission: Mission?
    
    var body: some View {
        HStack(spacing: 12) {
            if viewModel.isCompleting[index] == true {
                // Loading indicator for mission completion
                ProgressView()
                    .frame(width: 30, height: 30)
                    .padding(.vertical, 10)
            } else {
                Button(action: {
                    isCompleteConfirmationDialogPresented = true
                }) {
                    RoundedRectangle(cornerRadius: 6)
                        .fill(Color.gray.opacity(0.3))
                        .frame(width: 30, height: 30)
                        .overlay(
                            Image(systemName: "checkmark")
                                .foregroundColor(.white)
                                .opacity(viewModel.isRerolling[index] == true ? 1 : 0)
                        )
                        .shadow(radius: 1, x: 0, y: 1)
                }
                .buttonStyle(PlainButtonStyle())
                .confirmationDialog("¿Estás seguro que deseas marcar esta misión como completada?", isPresented: $isCompleteConfirmationDialogPresented, titleVisibility: .visible) {
                    Button("Confirmar") {
                        isMissionCompletedModalPresented = true
                        completedMission = mission
                        viewModel.postCompleteMission(userId: authViewModel.userId, prevMissionId: mission.id, prevMissionIndex: index)
                    }
                    Button("Cancelar", role: .cancel) {}
                }
            }
            
            Button(action: {
                isMissionDescriptionModalPresented = true
                selectedMission = mission
            }) {
                Text(mission.titulo)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(.primary)
                    .lineLimit(1)
            }
            
            Spacer()
            
            if viewModel.isRerolling[index] == true {
                ProgressView()
                    .frame(width: 20, height: 20)
                    .padding(.vertical, 10)
            } else {
                Button(action: {
                    isRerollConfirmationDialogPresented = true
                }) {
                    Image(systemName: "arrow.triangle.2.circlepath")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 20, height: 20)
                        .foregroundColor(.orange)
                }
                .buttonStyle(PlainButtonStyle())
                .confirmationDialog("¿Estás seguro que deseas obtener una nueva misión?", isPresented: $isRerollConfirmationDialogPresented, titleVisibility: .visible) {
                    Button("Confirmar") {
                        viewModel.postRerollOptMissions(userId: authViewModel.userId, prevMissionId: mission.id, prevMissionIndex: index)
                    }
                    Button("Cancelar", role: .cancel) {}
                }
            }
        }
        .padding(.vertical, 8)
        .padding(.horizontal, 12)
        .background(Color.white)
        .cornerRadius(10)
        .shadow(color: Color.black.opacity(0.1), radius: 2, x: 0, y: 1)
        .fullScreenCover(isPresented: $isMissionCompletedModalPresented) {
            MissionCompleteView(path: $path, isPresented: $isMissionCompletedModalPresented, isShareSheetPresented: $isShareSheetPresented, completedMission: $completedMission)
        }
    }
}
