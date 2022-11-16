export const handleInput = (event, user, setUser) => {
    const name = event.target.name;
    const value = event.target.value;
    setUser({ ...user, [name] : value});
}

export const handleChange = (event, user, setUser, selectedType, setSelectedType) => {
    const target = event.target;
    if (target.checked) {
        setSelectedType(target.value);
    }
    setUser({ ...user, type: target.value});
}