import "../styles/Filters.css";

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
    const [sortField, setSortField] = useState("date");
    const [sortOrder, setSortOrder] = useState("asc");

    const handleParamChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const handleSortChange = (field: string) => {
        if (sortField === field) {
            setSortOrder(sortOrder === "asc" ? "desc" : "asc");
        } else {
            setSortField(field);
            setSortOrder("asc");
        }

    };

    const buildQuery = () => {
        const queryParams = Object.entries(filters)
            .filter(([_, value]) => value !== "" && value !== null)
            .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
            .join("&");

        return `&${queryParams}&sort=${sortField},${sortOrder}`;
    };

    const handleSearch = () => {
        onSearch(buildQuery());
    };

    return (
        <div className="search-filters">
            <input type="text" name="departure" placeholder="Departure" onChange={handleParamChange} />
            <input type="text" name="destination" placeholder="Destination" onChange={handleParamChange} />
            <input type="date" name="startDate" onChange={handleParamChange} />
            <input type="date" name="endDate" onChange={handleParamChange} />
            <input type="number" name="minDuration" placeholder="Min duration (min)" onChange={handleParamChange} />
            <input type="number" name="maxDuration" placeholder="Max duration (min)" onChange={handleParamChange} />
            <input type="number" name="minPrice" placeholder="Min price (€)" onChange={handleParamChange} />
            <input type="number" name="maxPrice" placeholder="Max price (€)" onChange={handleParamChange} />
            <button onClick={handleSearch}>Search</button>
            <div className="sorting">
                <button onClick={() => handleSortChange("date")}>Sort by Date {sortField === "date" && (sortOrder === "asc" ? "↑" : "↓")}</button>
                <button onClick={() => handleSortChange("price")}>Sort by Price {sortField === "price" && (sortOrder === "asc" ? "↑" : "↓")}</button>
            </div>
        </div>

    );
}