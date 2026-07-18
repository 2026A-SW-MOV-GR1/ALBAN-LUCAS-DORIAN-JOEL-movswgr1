import { IRepository } from "../models/repository.interface";
import { Item } from "../models/item.model";
import { Logger } from "../utils/logger";
import { knownFolders, File } from "@nativescript/core";

export class NoSqlRepository implements IRepository {
    private file: File;
    private path: string;

    async init() {
        try {
            this.path = knownFolders.documents().path + "/data.json";
            this.file = File.fromPath(this.path);
            const content = this.file.readTextSync();
            if (!content || content.trim() === "") {
                this.file.writeTextSync("[]");
            }
            Logger.info("JSON NoSQL Inicializado en: " + this.path);
        } catch (e) {
            Logger.error("Error al inicializar NoSQL", e);
        }
    }

    async getAll(): Promise<Item[]> {
        try {
            const content = await this.file.readText();
            return JSON.parse(content);
        } catch (e) {
            Logger.error("Error leyendo archivo NoSQL", e);
            return [];
        }
    }

    async create(item: Item) {
        try {
            const items = await this.getAll();
            item.id = Date.now();
            items.push(item);
            await this.file.writeText(JSON.stringify(items));
            Logger.debug(`NoSQL Insert: ${item.name}`);
        } catch (e) {
            Logger.error("Error creando en NoSQL", e);
        }
    }

    async update(item: Item) {
        try {
            const items = await this.getAll();
            const index = items.findIndex(i => i.id === item.id);
            if (index !== -1) {
                items[index] = item;
                await this.file.writeText(JSON.stringify(items));
                Logger.debug(`NoSQL Update: ${item.name}`);
            }
        } catch (e) {
            Logger.error("Error actualizando en NoSQL", e);
        }
    }

    async delete(id: number | string) {
        try {
            let items = await this.getAll();
            items = items.filter(i => i.id !== id);
            await this.file.writeText(JSON.stringify(items));
            Logger.debug(`NoSQL Delete: ID ${id}`);
        } catch (e) {
            Logger.error("Error eliminando de NoSQL", e);
        }
    }
}
