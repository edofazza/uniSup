-record(message, {sender,
               receiver,
               text,
               timestamp    % An incremented value or a real date
              }).

-record(user, {username,
                  password,
                  nodeName,
                  pid
                }).