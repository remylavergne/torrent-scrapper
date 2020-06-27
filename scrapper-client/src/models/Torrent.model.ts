export interface Torrent {
    id?: string;
    category?: string;
    subCategory?: string;
    url?: string;
    filename?: string;
    commentsCount?: number;
    elapsedTimestamp?: number;
    size?: string;
    completions?: number;
    seeders?: number;
    leechers?: number;
    domain?: string;
}