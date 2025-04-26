import {Cell} from './cell.model';

export interface Board {
    size: number;
    cells: Cell[][];
    regionMap: number[][];
}
