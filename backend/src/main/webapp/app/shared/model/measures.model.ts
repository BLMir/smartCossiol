import { Moment } from 'moment';
import { IDevices } from 'app/shared/model/devices.model';

export interface IMeasures {
  id?: number;
  temp?: number;
  soil?: number;
  light?: number;
  insertAt?: Moment;
  devices?: IDevices;
}

export const defaultValue: Readonly<IMeasures> = {};
