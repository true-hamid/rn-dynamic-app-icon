@objc(DynamicAppIcon)
class DynamicAppIcon: NSObject {

    @objc
        static func requiresMainQueueSetup() -> Bool {
            return false
        }

        @available(iOS 10.3, *)
        @objc(changeIcon:withResolver:withRejecter:)
        func changeIcon(iconName: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
            if !UIApplication.shared.supportsAlternateIcons {
                reject("Error", "Alternate icon not supported", nil)
                return
            }
            let currentIcon = UIApplication.shared.alternateIconName ?? ""
            if iconName == currentIcon {
                reject("Error", "Icon already in use", nil)
                return
            }
            resolve(true)
            if iconName == "" {
                UIApplication.shared.setAlternateIconName(nil)
                return
            }
            setApplicationIconName(iconName)
        }

    @objc
        func setApplicationIconName(_ iconName: String) {
            if UIApplication.shared.responds(to: #selector(getter: UIApplication.supportsAlternateIcons)) && UIApplication.shared.supportsAlternateIcons {

                typealias setAlternateIconName = @convention(c) (NSObject, Selector, NSString, @escaping (NSError) -> ()) -> ()

                let selectorString = "_setAlternateIconName:completionHandler:"

                let selector = NSSelectorFromString(selectorString)
                let imp = UIApplication.shared.method(for: selector)
                let method = unsafeBitCast(imp, to: setAlternateIconName.self)
                method(UIApplication.shared, selector, iconName as NSString, { _ in })
            }
        }
}

