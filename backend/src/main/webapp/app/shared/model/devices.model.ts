import { IUser } from 'app/shared/model/user.model';
import { Type } from 'app/shared/model/enumerations/type.model';

export interface IDevices {
  id?: number;
  title?: string;
  type?: Type;
  identification?: string;
  active?: boolean;
  user?: IUser;
}

export const defaultValue: Readonly<IDevices> = {
  active: false
};
