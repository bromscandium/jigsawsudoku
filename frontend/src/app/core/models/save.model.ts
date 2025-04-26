import { User } from './user.model';

export interface Save {
    id: number;
    user: User;
    boardJson: string;
    attempts: number;
    timeSeconds: number;
    difficulty: string;
    size: number;
}
