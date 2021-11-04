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
  if (!packageName) packageName = 'null';
  try {
    const changeIcon = await DynamicAppIcon.changeIcon(iconName, packageName);
    return changeIcon;
  } catch (error) {
    throw error;
  }
}
