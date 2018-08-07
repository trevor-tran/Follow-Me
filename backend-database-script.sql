# create database
create database follow_me;
use follow_me;

#create tables
create table GroupInfo( group_id varchar(20) not null,
							leader_id varchar(50),
                            primary key(group_id));
                            
create table MemberInfo( member_id varchar(50) not null,
							member_name varchar(30),
                            platform varchar(10),
                            primary key (member_id));

create table TripInfo( trip_id int auto_increment not null,
							group_id varchar(20),
                            member_id varchar(50),
                            latitude double,
                            longitude double,
                            heading int(1),
                            speed int(1),
                            primary key(trip_id),
                            foreign key (group_id) references GroupInfo(group_id),
                            foreign key (member_id) references MemberInfo(member_id));

