import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDevices, defaultValue } from 'app/shared/model/devices.model';

export const ACTION_TYPES = {
  FETCH_DEVICES_LIST: 'devices/FETCH_DEVICES_LIST',
  FETCH_DEVICES: 'devices/FETCH_DEVICES',
  CREATE_DEVICES: 'devices/CREATE_DEVICES',
  UPDATE_DEVICES: 'devices/UPDATE_DEVICES',
  DELETE_DEVICES: 'devices/DELETE_DEVICES',
  RESET: 'devices/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDevices>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type DevicesState = Readonly<typeof initialState>;

// Reducer

export default (state: DevicesState = initialState, action): DevicesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DEVICES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DEVICES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DEVICES):
    case REQUEST(ACTION_TYPES.UPDATE_DEVICES):
    case REQUEST(ACTION_TYPES.DELETE_DEVICES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DEVICES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DEVICES):
    case FAILURE(ACTION_TYPES.CREATE_DEVICES):
    case FAILURE(ACTION_TYPES.UPDATE_DEVICES):
    case FAILURE(ACTION_TYPES.DELETE_DEVICES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DEVICES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DEVICES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DEVICES):
    case SUCCESS(ACTION_TYPES.UPDATE_DEVICES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DEVICES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/devices';

// Actions

export const getEntities: ICrudGetAllAction<IDevices> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DEVICES_LIST,
  payload: axios.get<IDevices>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDevices> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DEVICES,
    payload: axios.get<IDevices>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDevices> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DEVICES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDevices> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DEVICES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDevices> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DEVICES,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
