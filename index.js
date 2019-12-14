import { NativeEventEmitter, NativeModules } from 'react-native';

const { RNCaptureTraffic } = NativeModules;
const eventEmitter = new NativeEventEmitter(RNCaptureTraffic);

const eventMap = {
    serviceStarted: 'onServiceStarted',
    serviceStopped: 'onServiceStopped',
};
const _subscriptions = new Map();

const addEventListener = (event, handler) => {
    const mappedEvent = eventMap[event];
    if (mappedEvent) {
      const listener = eventEmitter.addListener(mappedEvent, handler);
      _subscriptions.set(handler, listener);
      return {
        remove: () => removeEventListener(event, handler),
      };
    } else {
      console.warn(`Trying to subscribe to unknown event: "${event}"`);
      return {
        remove: () => {},
      };
    }
  };
  
  const removeEventListener = (type, handler) => {
    const listener = _subscriptions.get(handler);
    if (!listener) {
      return;
    }
    listener.remove();
    _subscriptions.delete(handler);
  };
  
  const removeAllListeners = () => {
    _subscriptions.forEach((listener, key, map) => {
      listener.remove();
      map.delete(key);
    });
  };

export default {
    ...RNCaptureTraffic,
    addEventListener,
    removeEventListener,
    removeAllListeners,
};
