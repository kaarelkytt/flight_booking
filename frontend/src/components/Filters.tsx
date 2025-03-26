import { useState } from "react";

type SearchParams = {
    departure: string;
    destination: string;
    startDate: string;
    endDate: string;
    minDuration: number | "";
    maxDuration: number | "";
    minPrice: number | "";
    maxPrice: number | "";
};

export default function Filters({ onSearch }: { onSearch: (query: string) => void }) {
    const [filters, setFilters] = useState<SearchParams>({
        departure: "",
        destination: "",
        startDate: "",
        endDate: "",
        minDuration: "",
        maxDuration: "",
        minPrice: "",
        maxPrice: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const buildQuery = () => {
        const queryParams = Object.entries(filters)
            .filter(([_, value]) => value !== "" && value !== null)
            .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
            .join("&");

        return queryParams ? `&${queryParams}` : "";
    };

    const handleSearch = () => {
        onSearch(buildQuery());
    };

    return (
        <div className="search-filters">
            <input type="text" name="departure" placeholder="Departure" onChange={handleChange} />
            <input type="text" name="destination" placeholder="Destination" onChange={handleChange} />
            <input type="date" name="startDate" onChange={handleChange} />
            <input type="date" name="endDate" onChange={handleChange} />
            <input type="number" name="minDuration" placeholder="Min duration (min)" onChange={handleChange} />
            <input type="number" name="maxDuration" placeholder="Max duration (min)" onChange={handleChange} />
            <input type="number" name="minPrice" placeholder="Min price (€)" onChange={handleChange} />
            <input type="number" name="maxPrice" placeholder="Max price (€)" onChange={handleChange} />
            <button onClick={handleSearch}>Search</button>
        </div>
    );
}