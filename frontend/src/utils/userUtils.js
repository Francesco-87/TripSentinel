// Return the matching user, or undefined when that ID is not loaded.
export function matchUserToId(users, userId) {
    return users.find(user => user.id === userId);
}
