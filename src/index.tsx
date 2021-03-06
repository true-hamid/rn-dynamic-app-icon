import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-dynamic-app-icon' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const DynamicAppIcon = NativeModules.DynamicAppIcon
  ? NativeModules.DynamicAppIcon
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export async function changeIcon(
  iconName: string,
  packageName?: string
): Promise<number> {
  try {
    let changeIconNative;
    if (Platform.OS === 'android') {
      if (!packageName) packageName = 'null';
      changeIconNative = await DynamicAppIcon.changeIcon(iconName, packageName);
    } else {
      changeIconNative = await DynamicAppIcon.changeIcon(iconName);
    }
    return changeIconNative;
  } catch (error) {
    throw error;
  }
}
