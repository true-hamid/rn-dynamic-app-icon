import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  FlatList,
  Image,
} from 'react-native';
import { changeIcon } from 'rn-dynamic-app-icon';

type ItemProps = {
  flag: images;
  iconChangeParam: string;
};

type images = 'au' | 'ca' | 'uk' | 'us' | 'sd';
type icons = 'AU' | 'CA' | 'UK' | 'US' | '';

type ImagesMap = {
  [key in images]: JSX.Element;
};

type dataObject = {
  flag: images;
  iconChangeParam: icons;
};

type dataType = Array<dataObject>;

export default function App() {
  const data: dataType = [
    {
      flag: 'au',
      iconChangeParam: 'AU',
    },
    {
      flag: 'ca',
      iconChangeParam: 'CA',
    },
    {
      flag: 'uk',
      iconChangeParam: 'UK',
    },
    {
      flag: 'us',
      iconChangeParam: 'US',
    },
  ];

  const renderFlagImage = (name: images) => {
    const images: ImagesMap = {
      au: <Image style={styles.image} source={require('./assets/au.png')} />,
      ca: <Image style={styles.image} source={require('./assets/ca.png')} />,
      uk: <Image style={styles.image} source={require('./assets/uk.png')} />,
      us: <Image style={styles.image} source={require('./assets/us.png')} />,
      sd: <Image style={styles.image} source={require('./assets/sd.png')} />,
    };
    return images[name];
  };

  const IconItem = ({ flag, iconChangeParam }: ItemProps) => {
    return (
      <TouchableOpacity
        style={styles.itemContainer}
        onPress={() => {
          changeIcon(iconChangeParam);
        }}
      >
        {renderFlagImage(flag)}
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.itemContainer}>
        <Text style={styles.text}>Default</Text>
        <IconItem flag="sd" iconChangeParam="" />
      </View>
      <FlatList
        data={data}
        numColumns={2}
        scrollEnabled={false}
        //@ts-ignore
        keyExtractor={(item, index) => index.toString()}
        //@ts-ignore
        renderItem={({ item }: dataObject) => (
          <IconItem flag={item.flag} iconChangeParam={item.iconChangeParam} />
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    height: '100%',
    alignItems: 'stretch',
    justifyContent: 'center',
  },
  itemContainer: {
    flex: 1,
    alignItems: 'center',
  },
  image: {
    width: 128,
    height: 128,
  },
  text: {
    fontWeight: '700',
  },
});
