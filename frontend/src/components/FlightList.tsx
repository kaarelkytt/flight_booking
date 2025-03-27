import "../styles/Pagination.css";
import ReactPaginate from "react-paginate";
import { useEffect, useState } from "react";
import { fetchFlights } from "../api";
import { FlightPage } from "../types";
import Filters from "./Filters";
import FlightRow from "./FlightRow";

export default function FlightList({ onSelect }: { onSelect: (id: number) => void }) {
    const [flightPage, setFlightPage] = useState<FlightPage | null>(null);
    const [query, setQuery] = useState("");
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);

    const updateFlights = () => {
        fetchFlights(`?page=${currentPage}&size=${pageSize}${query}`)
            .then(setFlightPage)
            .catch(err => console.error(err));
    };

    useEffect(updateFlights, [currentPage, pageSize, query]);

    useEffect(() => {
        setCurrentPage(0);
    }, [query]);

    return (
        <div>
            <Filters onSearch={setQuery} />

            <div className="page-size-selector">
                <label htmlFor="pageSize">Results per page:</label>
                <select
                    id="pageSize"
                    value={pageSize}
                    onChange={(e) => {
                        setPageSize(Number(e.target.value));
                        setCurrentPage(0);
                    }}
                >
                    <option value="10">10</option>
                    <option value="15">15</option>
                    <option value="25">25</option>
                </select>
            </div>

            {flightPage?.content.map((flight, index) => (
                <FlightRow
                    key={flight.id}
                    index={index + 1 + pageSize * currentPage}
                    flightNumber={flight.flightNumber}
                    departureCity={flight.departureCity}
                    destinationCity={flight.destinationCity}
                    departureTime={flight.departureTime}
                    arrivalTime={flight.arrivalTime}
                    durationMinutes={flight.durationMinutes}
                    initialPrice={flight.initialPrice}
                    onSelect={() => onSelect(flight.id)}
                />
            ))}

            <ReactPaginate
                previousLabel={"Previous"}
                nextLabel={"Next"}
                breakLabel={"..."}
                pageCount={flightPage?.totalPages || 1}
                marginPagesDisplayed={2}
                pageRangeDisplayed={3}
                onPageChange={(selected) => setCurrentPage(selected.selected)}
                forcePage={currentPage}
                containerClassName={"pagination"}
                activeClassName={"active"}
            />
        </div>
    );
}