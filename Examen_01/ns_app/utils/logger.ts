export class Logger {
    static info(msg: string) {
        console.log(`[INFO] ${new Date().toISOString()}: ${msg}`);
    }

    static debug(msg: string) {
        console.log(`[DEBUG] ${new Date().toISOString()}: ${msg}`);
    }

    static error(msg: string, err?: any) {
        console.error(`[ERROR] ${new Date().toISOString()}: ${msg}`, err || "");
    }
}
