import { IRepository } from "../models/repository.interface";
import { Item } from "../models/item.model";
import { Logger } from "../utils/logger";

const Sqlite = require("nativescript-sqlite");

export class SqliteRepository implements IRepository {
    private db: any;

    async init() {
        try {
            this.db = await new Sqlite("my_db.sqlite");
            await this.db.execSQL("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT)");
            Logger.info("SQLite Initializado y Tabla Creada");
        } catch (e) {
            Logger.error("Error al inicializar SQLite", e);
        }
    }

    async getAll(): Promise<Item[]> {
        try {
            const rows = await this.db.all("SELECT * FROM items");
            return rows.map(r => ({ id: r[0], name: r[1], description: r[2] }));
        } catch (e) {
            Logger.error("Error obteniendo items de SQLite", e);
            return [];
        }
    }

    async create(item: Item) {
        try {
            await this.db.execSQL("INSERT INTO items (name, description) VALUES (?, ?)", [item.name, item.description]);
            Logger.debug(`SQL Insert: ${item.name}`);
        } catch (e) {
            Logger.error("Error insertando en SQLite", e);
        }
    }

    async update(item: Item) {
        try {
            await this.db.execSQL("UPDATE items SET name = ?, description = ? WHERE id = ?", [item.name, item.description, item.id]);
            Logger.debug(`SQL Update: ${item.name}`);
        } catch (e) {
            Logger.error("Error actualizando en SQLite", e);
        }
    }

    async delete(id: number | string) {
        try {
            await this.db.execSQL("DELETE FROM items WHERE id = ?", [id]);
            Logger.debug(`SQL Delete: ID ${id}`);
        } catch (e) {
            Logger.error("Error eliminando de SQLite", e);
        }
    }
}
