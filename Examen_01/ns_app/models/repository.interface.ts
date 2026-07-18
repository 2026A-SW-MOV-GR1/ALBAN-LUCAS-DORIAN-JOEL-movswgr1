import { Item } from "./item.model";

export interface IRepository {
    init(): Promise<void>;
    getAll(): Promise<Item[]>;
    create(item: Item): Promise<void>;
    update(item: Item): Promise<void>;
    delete(id: number | string): Promise<void>;
}
