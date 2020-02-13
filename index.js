import { NativeModules } from 'react-native';
import { electrodeBridge } from 'react-native-electrode-bridge';
const { RNCaptureTraffic } = NativeModules;
const _subscriptions = Array();

const vpnStarted = (handler) => {
  const eventId = electrodeBridge.registerEventListener("onServiceStarted", handler);
  _subscriptions.push(eventId);
  return {
    remove: () => removeEventListener(eventId),
  };
};
const vpnStopped = (handler) => {
  const eventId = electrodeBridge.registerEventListener("onServiceStopped", handler);
  _subscriptions.push(eventId);
  return {
    remove: () => removeEventListener(eventId),
  };
};

const removeEventListener = (eventId) => {
  electrodeBridge.removeEventListener(eventId)
  _subscriptions = _subscriptions.filter(item => item !== eventId)
};

const removeAllListeners = () => {
  _subscriptions.forEach((key, index, map) => {
    electrodeBridge.removeEventListener(key);
    map.delete(key);
  });
};

export default {
    ...RNCaptureTraffic,
    vpnStarted,
    vpnStopped,
    removeEventListener,
    removeAllListeners,
};
