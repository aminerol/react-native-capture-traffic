import {NativeModules} from 'react-native';
import {registerEvent, registerRequest, removeAllListeners} from './utils';
const {RNCaptureTraffic} = NativeModules;

const vpnStarted = handler => {
  return registerEvent('onServiceStarted', handler);
};
const vpnStopped = handler => {
  return registerEvent('onServiceStopped', handler);
};

const shouldIntercept = handler => {
  return registerRequest('shouldIntercept', handler);
};
const onRequestHeaders = handler => {
  return registerEvent('onRequestHeaders', handler);
};
const onRequestBody = handler => {
  return registerRequest('onRequestBody', handler);
};

export default {
  ...RNCaptureTraffic,
  vpnStarted,
  vpnStopped,
  shouldIntercept,
  onRequestHeaders,
  onRequestBody,
  removeAllListeners,
};
