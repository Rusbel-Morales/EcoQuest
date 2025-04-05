//
//  MissionItemViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/14/24.
//

import SwiftUI
import Combine

class MissionItemViewModel: ObservableObject {
    @Published var isChecked: Bool = false
    @Published var showModal: Bool = false
    
    private let missionId: Int
    private let userId: String
    let misionesViewModel: MisionesViewModel
    
    init(missionId: Int, userId: String, misionesViewModel: MisionesViewModel) {
        self.missionId = missionId
        self.userId = userId
        self.misionesViewModel = misionesViewModel
        self.isChecked = misionesViewModel.isMissionCompleted
    }
    
    func toggleMissionCompletion() {
        guard !isChecked else { return }
        isChecked = true
        misionesViewModel.markMissionAsComplete(missionId: missionId, userId: userId)
    }
    
    func presentDescriptionModal() {
        showModal = true
    }
}
