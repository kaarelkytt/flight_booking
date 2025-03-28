import {useEffect, useState} from "react";
import Autosuggest from "react-autosuggest";
import {fetchCities} from "../api";

export default function AutoComplete({type, onSelect}: {
    type: "departure" | "destination";
    onSelect: (value: string) => void
}) {
    const [query, setQuery] = useState("");
    const [suggestions, setSuggestions] = useState<string[]>([]);

    useEffect(() => {
        updateSuggestions();
    }, [query, type]);

    const updateSuggestions = () => {
        if (query === "") {
            setSuggestions([]);
        } else {
            fetchCities(type, query)
                .then(setSuggestions)
                .catch(err => console.error(err));
        }
    }

    const renderSuggestion = (suggestion: string) => (
        <div>{suggestion}</div>
    );

    return (
        <div className="autocomplete-container">
            <Autosuggest
                suggestions={suggestions}
                onSuggestionsFetchRequested={() => updateSuggestions()}
                onSuggestionsClearRequested={() => setSuggestions([])}
                getSuggestionValue={(suggestion: string) => suggestion}
                renderSuggestion={renderSuggestion}
                inputProps={{
                    placeholder: `Enter ${type} city`,
                    value: query,
                    onChange: (_, {newValue}) => {
                        setQuery(newValue);
                        onSelect(newValue);},
                }}
                onSuggestionSelected={(_, {suggestionValue}) => {
                    setQuery(suggestionValue);
                    onSelect(suggestionValue);
                }}
            />
        </div>
    );
}