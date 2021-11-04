import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { changeIcon } from 'rn-dynamic-app-icon';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    
    // multiply(3, 7).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <TouchableOpacity onPress={() => {changeIcon('Islami')}}><Text>Islami</Text></TouchableOpacity>
      <TouchableOpacity onPress={() => {changeIcon('Neo')}}><Text>Neo</Text></TouchableOpacity>
      <TouchableOpacity onPress={() => {changeIcon('')}}><Text>Default</Text></TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
