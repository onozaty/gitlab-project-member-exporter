# gitlab-project-member-exporter

A tool that exports the list of members of each project of GitLab.

## Usage

Java (JDK8 or higher) is required for execution.

Download the latest jar file (`gitlab-project-member-exporter-x.x.x-all.jar`) from below.

* https://github.com/onozaty/gitlab-project-member-exporter/releases/latest

Execute the application with the following command.

```
java -jar gitlab-project-member-exporter-1.0.0-all.jar -c config.json -pm project-members.csv -p projects.csv -u users.csv
```

Use GitLab API to export the following information as a CSV file.

* Project member list
* Project list
* User list

## Command description

```console
usage: java -jar gitlab-project-member-exporter-all.jar -c <config file> [-pm <project members file>] [-p <projects file>] [-u <users file>]
 -c <config file>             Config file path.
 -pm <project members file>   Project members export file path.
 -p <projects file>           Projects export file path.
 -u <users file>              Users export file path.
```

You can specify the following as an argument.

### `-c <config file>`

Configuration file path. This is required.

Set the GitLab URL and personal access token in the configuration file.

```json
{
  "gitLabUrl": "https://gitlab.example.com",
  "personalAccessToken": "Dw7T7R37NEQRgYiMp4uT"
}
```

See below for how to obtain a personal access token.

* [Personal access tokens \| GitLab](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html)

### `-pm <project members file>`

Output file path of the project member list.  
The following information is output in CSV format.

* `Project: Name`
* `Project: Visibility`
* `User: Name`
* `User: Username`
* `User: State`
* `Permission`

```csv
Project: Name,Project: Visibility,User: Name,User: Username,User: State,Permission
Administrator / my-project,private,Administrator,root,active,Maintainer
group1 / project-b,private,Administrator,root,active,Owner
group1 / project-b,private,user2,user2,active,Developer
group1 / project-a,private,Administrator,root,active,Owner
group1 / project-a,private,user2,user2,active,Developer
group1 / project-a,private,user1,user1,blocked,Maintainer
```

| Project: Name              | Project: Visibility | User: Name    | User: Username | User: State | Permission |
|----------------------------|---------------------|---------------|----------------|-------------|------------|
| Administrator / my-project | private             | Administrator | root           | active      | Maintainer |
| group1 / project-b         | private             | Administrator | root           | active      | Owner      |
| group1 / project-b         | private             | user2         | user2          | active      | Developer  |
| group1 / project-a         | private             | Administrator | root           | active      | Owner      |
| group1 / project-a         | private             | user2         | user2          | active      | Developer  |
| group1 / project-a         | private             | user1         | user1          | blocked     | Maintainer |

If authority is granted to the group, it is output that each project under the group has authority.

### `-p <projects file>`

Output file path of the project list.  
The following information is output in CSV format.

* `Name`
* `Visibility`
* `Created at`
* `Last activity at`

```csv
Name,Visibility,Created at,Last activity at
Administrator / my-project,private,2019-11-16T13:48:47.727Z,2019-11-16T13:48:47.727Z
group1 / project-b,private,2019-11-16T13:48:11.161Z,2019-11-24T15:08:11.024Z
group1 / project-a,private,2019-11-16T13:44:20.849Z,2019-11-16T13:44:20.849Z
```

| Name                       | Visibility | Created at               | Last activity at         |
|----------------------------|------------|--------------------------|--------------------------|
| Administrator / my-project | private    | 2019-11-16T13:48:47.727Z | 2019-11-16T13:48:47.727Z |
| group1 / project-b     | private    | 2019-11-16T13:48:11.161Z | 2019-11-24T15:08:11.024Z |
| group1 / project-a     | private    | 2019-11-16T13:44:20.849Z | 2019-11-16T13:44:20.849Z |

### `-u <users file>`

Output file path of the user list.  
The following information is output in CSV format.

* `Name`
* `Username`
* `Email`
* `State`
* `Admin`
* `Created at`
* `Last sign in at`
* `Last activity on`

```csv
Name,Username,Email,State,Admin,Created at,Last sign in at,Last activity on
user3,user3,user3@example.com,active,false,2019-11-16T13:37:15.885Z,,
user2,user2,user2@example.com,active,false,2019-11-16T13:36:29.651Z,,
user1,user1,user1@example.com,blocked,false,2019-11-16T13:35:44.406Z,,
Administrator,root,admin@example.com,active,true,2019-11-16T07:50:47.821Z,2019-11-24T14:50:54.996Z,2019-11-25
```

| Name          | Username | Email             | State   | Admin | Created at               | Last sign in at          | Last activity on |
|---------------|----------|-------------------|---------|-------|--------------------------|--------------------------|------------------|
| user3         | user3    | user3@example.com | active  | FALSE | 2019-11-16T13:37:15.885Z |                          |                  |
| user2         | user2    | user2@example.com | active  | FALSE | 2019-11-16T13:36:29.651Z |                          |                  |
| user1         | user1    | user1@example.com | blocked | FALSE | 2019-11-16T13:35:44.406Z |                          |                  |
| Administrator | root     | admin@example.com | active  | TRUE  | 2019-11-16T07:50:47.821Z | 2019-11-24T14:50:54.996Z | 2019-11-25       |

## How to build

When building from the source code, build the application with the following command in the environment where Java (JDK 8 or higher) is installed.

```
gradlew shadowJar
```

`build/libs/gitlab-project-member-exporter-x.x.x-all.jar` will be created. (`x.x.x` is version number)
