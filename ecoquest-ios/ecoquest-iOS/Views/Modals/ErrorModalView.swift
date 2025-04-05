//
//  ErrorModalView.swift
//  ecoquest-iOS
//
//  Created by Administrador on 27/09/24.
//

import SwiftUI
import AlertToast

struct GlobalErrorView {
    static func showError(_ message: String, in view: Binding<Bool>) -> AlertToast {
        AlertToast(type: .error(Color.red), title: message)
    }
}
