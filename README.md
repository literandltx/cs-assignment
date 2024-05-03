## Quick start
1. Must be installed java 21, maven
2. Build the Application
```
mvn clean package
```
3. Run the Application
```
mvn spring-boot:run -Dspring.profiles.active=dev
```

## User Model
### Attributes

- **id**: `Long`
- **email**: `String`
- **firstname**: `String`
- **lastname**: `String`
- **birthdate**: `LocalDate`
- **address**: `String`
- **phoneNumber**: `String`

### Database Mapping

- Mapped to `users` table
- `id` as primary key, auto-generated
- `email`, `firstname`, `lastname`, `birthdate` as required fields
- `address` and `phoneNumber` optional

## Requests example

#### `Create user` POST `/api/v1/users`, optional fields `address` and `phoneNumber`
Create new user
```json
{
    "email": "example@email.com",
    "firstname": "firstname",
    "lastname": "lastname",
    "birthdate": "2002-12-12",
    "phoneNumber": "38099xxxxxxx"
}
```

#### `Update one/some user fields` PATCH `/api/v1/users?id=userID`, all fields are optional
Update some user fields by userID if user exist
```json
{
    "email": "newemail@email.com",
     "birthdate": "2000-01-01"
}
```

#### `Update all user fields` PUT `/api/v1/users?id=userID`, optional fields `address` and `phoneNumber`
Update all user fields by userID if user exist
```json
{
    "email": "newemail@email.com",
    "firstname": "updated_name",
    "lastname": "lastname1",
    "birthdate": "2002-12-03",
    "address": "new_address1"
}
```

#### `Delete user` DELETE `/api/v1/user?id=userID`
Delete user by userID if user exist

#### `Search for users by birth date range` GET `/api/v1/user/search?from=2000-01-01&to-2005-01-01&page=0&size=10`
Return list of users in birt date range `(from, to)`, `from` and `to` are required. Pagination supported, `page` and `size` are optional