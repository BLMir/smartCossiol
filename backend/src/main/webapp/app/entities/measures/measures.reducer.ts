import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMeasures, defaultValue } from 'app/shared/model/measures.model';

export const ACTION_TYPES = {
  FETCH_MEASURES_LIST: 'measures/FETCH_MEASURES_LIST',
  FETCH_MEASURES: 'measures/FETCH_MEASURES',
  CREATE_MEASURES: 'measures/CREATE_MEASURES',
  UPDATE_MEASURES: 'measures/UPDATE_MEASURES',
  DELETE_MEASURES: 'measures/DELETE_MEASURES',
  RESET: 'measures/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMeasures>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MeasuresState = Readonly<typeof initialState>;

// Reducer

export default (state: MeasuresState = initialState, action): MeasuresState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEASURES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEASURES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEASURES):
    case REQUEST(ACTION_TYPES.UPDATE_MEASURES):
    case REQUEST(ACTION_TYPES.DELETE_MEASURES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MEASURES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEASURES):
    case FAILURE(ACTION_TYPES.CREATE_MEASURES):
    case FAILURE(ACTION_TYPES.UPDATE_MEASURES):
    case FAILURE(ACTION_TYPES.DELETE_MEASURES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEASURES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEASURES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEASURES):
    case SUCCESS(ACTION_TYPES.UPDATE_MEASURES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEASURES):
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

const apiUrl = 'api/measures';

// Actions

export const getEntities: ICrudGetAllAction<IMeasures> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEASURES_LIST,
  payload: axios.get<IMeasures>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMeasures> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEASURES,
    payload: axios.get<IMeasures>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMeasures> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEASURES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMeasures> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEASURES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMeasures> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEASURES,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
