import {Identity} from './identity';
import {Category} from './category';

export interface Streamer {

    id: number;
    identityId: number;
    firstname: string;
    lastname: string;
    patronymic: string;
    about: string;
    imageId: number;
    identity: Identity;
    categories: Category[];
}
