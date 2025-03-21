import {Authority} from './authority';

export interface Identity {

    id: number;
    nickname: string;
    password: string;
    email: string;
    enable: boolean;
    authority: Authority;
    imageId: number;
}
