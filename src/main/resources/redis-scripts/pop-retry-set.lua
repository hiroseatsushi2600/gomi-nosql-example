local keys = redis.call('ZRANGEBYSCORE', KEYS[1], '-inf', ARGV[1], 'LIMIT', 0, 1)
for _, key in ipairs(keys) do
    redis.call('ZREM', KEYS[1], key)
end
return keys