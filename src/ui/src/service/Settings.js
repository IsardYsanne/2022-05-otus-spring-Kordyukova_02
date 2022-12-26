const CONTROLLER = '/book';
const ENDPOINT_SOCKET='/ws-message';

// eslint-disable-next-line import/prefer-default-export
export const getApiUrl = (endpoint) => {
    const baseApiUrl = `http://${process.env.REACT_APP_URL}`;
    return `${baseApiUrl}${CONTROLLER}${endpoint}`;
};

export const getSocketUrl = () => {
    const baseApiWsUrl = `ws://${process.env.REACT_APP_URL}`;
    return `${baseApiWsUrl}${ENDPOINT_SOCKET}`;
};