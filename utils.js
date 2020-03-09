import {electrodeBridge} from 'react-native-electrode-bridge';
let _subscriptions = [];

const removeEventListener = eventId => {
  electrodeBridge.removeEventListener(eventId);
  _subscriptions = _subscriptions.filter(item => item !== eventId);
};

export const removeAllListeners = () => {
  _subscriptions.forEach((key, index, map) => {
    electrodeBridge.removeEventListener(key);
    _subscriptions = _subscriptions.filter(item => item !== key);
  });
};

export const registerEvent = (eventName, handler) => {
  const eventId = electrodeBridge.registerEventListener(eventName, handler);
  _subscriptions.push(eventId);
  return {
    remove: () => removeEventListener(eventId),
  };
};

export const registerRequest = (requestName, handler) => {
  electrodeBridge.registerRequestHandler(requestName, handler);
};
