//
//  Config.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/8/24.
//

import Foundation

struct Config {
    static var serverURL: String {
        guard let path = Bundle.main.path(forResource: "Config", ofType: "plist"),
              let config = NSDictionary(contentsOfFile: path),
              let url = config["server_url"] as? String else {
            fatalError("Server URL not set in Config.plist")
        }
        return url
    }
}
